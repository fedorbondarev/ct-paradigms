(defn divideWithInfinity [a & tail]
  (let [numer (if (empty? tail) 1 a)
        denom (if (empty? tail) a (apply * tail))]
    (if (zero? denom)
      ##Inf
      (/ numer denom)
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

(defn buildUnaryExpression [func]
  (fn [expr]
    (fn [vars] (func (expr vars)))
    )
  )

(def add (buildExpression +))
(def subtract (buildExpression -))
(def multiply (buildExpression *))
(def divide (buildExpression divideWithInfinity))
(def negate (buildUnaryExpression -))

(def exp (buildUnaryExpression #(Math/exp %)))
(def ln (buildUnaryExpression #(Math/log %)))

(def operators {
                '+      add
                '-      subtract
                '*      multiply
                '/      divide
                'negate negate
                'exp    exp
                'ln     ln
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