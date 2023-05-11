(defn divideWithInfinity [a & tail]
  (let [numer (if (empty? tail) 1 a)
        denom (if (empty? tail) a (apply * tail))]
    (if (zero? denom)
      ##Inf
      (/ numer denom)
      )
    )
  )

(defn meansqFunc [& v]
  (let [n (count v)]
    (if (= 0 n)
      0
      (/ (apply + (mapv #(* % %) v)) n)
      )
    )
  )

(defn constant [val] (fn [_vars] val))
(defn variable [name] (fn [vars] (get vars name)))

(defn buildExpression [func]
  (fn [& expressions]
    (fn [vars]
      (apply func
             (mapv #(% vars) expressions)
             )
      )
    )
  )

(def add (buildExpression +))
(def subtract (buildExpression -))
(def multiply (buildExpression *))
(def divide (buildExpression divideWithInfinity))
(def negate (buildExpression -))

(def exp (buildExpression #(Math/exp %)))
(def ln (buildExpression #(Math/log %)))

(def meansq (buildExpression meansqFunc))
(def rms (buildExpression
           (fn [& v]
             (Math/sqrt (apply meansqFunc v))
             )
           )
  )

(def operators {
                '+      add
                '-      subtract
                '*      multiply
                '/      divide
                'negate negate

                'exp    exp
                'ln     ln

                'meansq meansq
                'rms    rms
                }
  )

(defn parseFromList [el] (
                           cond
                           (list? el) (apply
                                        (get operators (first el))
                                        (mapv parseFromList (rest el))
                                        )
                           (number? el) (constant el)
                           (symbol? el) (variable (name el))
                           ))

(defn parseFunction [s] (
                          let [el (read-string s)]
                          (parseFromList el)
                          ))