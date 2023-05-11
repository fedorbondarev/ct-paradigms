(defn
  buildVectorFunction [func]
  (fn
    ([a b]
     (mapv func a b))
    )
  )

(def v+ (buildVectorFunction +))
(def v- (buildVectorFunction -))
(def v* (buildVectorFunction *))
(def vd (buildVectorFunction /))

(defn buildShapelessFunction [func]
  (letfn [
          (shapelessFunction [a b]
            (cond
              (and (vector? a) (vector? b)) (mapv shapelessFunction a b)
              :else (func a b)
              )
            )
          ]
    shapelessFunction
    )
  )

(def s+ (buildShapelessFunction +))
(def s- (buildShapelessFunction -))
(def s* (buildShapelessFunction *))
(def sd (buildShapelessFunction /))

(defn scalar [x y]
  (apply + (mapv * x y)))

(defn vect [x y]
  [
   (-
     (* (nth x 1) (nth y 2))
     (* (nth x 2) (nth y 1))
     )
   (-
     (* (nth x 2) (nth y 0))
     (* (nth x 0) (nth y 2))
     )
   (-
     (* (nth x 0) (nth y 1))
     (* (nth x 1) (nth y 0))
     )
   ]
  )

(defn v*s [v s]
  (mapv #(* % s) v))

(defn m*s [m s]
  (mapv #(v*s % s) m))

(def m+ (buildVectorFunction v+))
(def m- (buildVectorFunction v-))
(def m* (buildVectorFunction v*))
(def md (buildVectorFunction vd))

(defn m*v [m v]
  (mapv #(scalar % v) m)
  )

(defn transpose [m]
  (if (empty? m)
    []
    (let
      [s1 (range 0 (count m))
       s2 (range 0 (count (nth m 0)))]
      (mapv
        (fn [j]
          (mapv
            (fn [i] (nth (nth m i) j))
            s1
            )
          )
        s2
        )
      )
    )
  )

(defn m*m [m1 m2] (
                    let [t (transpose m2)
                         s1 (range 0 (count m1))
                         s2 (range 0 (count t))]
                    (mapv
                      (fn [i]
                        (mapv
                          (fn [j] (scalar (nth m1 i) (nth t j)))
                          s2
                          )
                        )
                      s1
                      )
                    )
  )