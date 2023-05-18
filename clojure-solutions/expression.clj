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

(defn createParser [constant variable operators]
  (letfn [
          (parseFromList [el] (
                                cond
                                (list? el) (apply
                                             (get operators (first el))
                                             (mapv parseFromList (rest el))
                                             )
                                (number? el) (constant el)
                                (symbol? el) (variable (name el))
                                ))]
    (fn [s]
      (parseFromList (read-string s))
      )
    ))

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

(def functionOperators {
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

(def parseFunction (createParser constant variable functionOperators))

(definterface IExpression
  (^Number evaluate [vars])
  ;(^Number diff [diffVarName])
  )

(deftype _Constant [value]
  Object
  (toString [_this] (str value))
  IExpression
  (evaluate [_this _vars] value)
  ;(diff [_this _diffVarName] (_Constant. 0))
  )

(deftype _Variable [name]
  Object
  (toString [_this] name)
  IExpression
  (evaluate [_this vars] (get vars name))
  ;(diff [_this diffVarName]
  ;  (if (= diffVarName name)
  ;    (_Constant. 1)
  ;    (_Constant. 0)))
  )

(defn evaluateAndApply [expressions vars fun]
  (apply fun
         (mapv #(.evaluate % vars) expressions)))

(defn expressionToString [operator children]
  (str "(" operator " "
       (clojure.string/join " "
                            (mapv #(.toString %) children))
       ")"))

(defn applyDiff [children diffVarName diffRule]
  (diffRule children
            (mapv #(.diff % diffVarName) children)))

(defn applyRightFoldDiff [children diffVarName diffRule foldRule]
  (cond
    (= (count children) 2)
    (let [
          f (first children)
          g (second children)
          f' (.diff f diffVarName)
          g' (.diff g diffVarName)]
      (diffRule f g f' g'))
    :else
    (let [
          f (first children)
          g (foldRule (rest children))
          f' (.diff f diffVarName)
          g' (.diff g diffVarName)
          ]
      (diffRule f g f' g')
      )))

(deftype _Add [expressions]
  Object
  (toString [_this] (expressionToString "+" expressions))
  IExpression
  (evaluate [_this vars]
    (evaluateAndApply expressions vars +))
  ;(diff [_this diffVarName]
  ;  (applyDiff expressions diffVarName
  ;             (fn [_exprs diffExprs] (_Add. diffExprs))))
  )

(deftype _Subtract [expressions]
  Object
  (toString [_this] (expressionToString "-" expressions))
  IExpression
  (evaluate [_this vars]
    (evaluateAndApply expressions vars -))
  ;(diff [_this diffVarName]
  ;  (applyDiff expressions diffVarName
  ;             (fn [_exprs diffExprs] (_Subtract. diffExprs))))
  )

(deftype _Multiply [expressions]
  Object
  (toString [_this] (expressionToString "*" expressions))
  IExpression
  (evaluate [_this vars]
    (evaluateAndApply expressions vars *))
  ;(diff [_this diffVarName]
  ;  (applyRightFoldDiff expressions diffVarName
  ;                      (fn [f g f' g']
  ;                        (_Add.
  ;                          [(_Multiply. [f' g])
  ;                           (_Multiply. [g' f])]))
  ;                      (fn [& args] (_Multiply. args))))
  )

(deftype _Divide [expressions]
  Object
  (toString [_this] (expressionToString "/" expressions))
  IExpression
  (evaluate [_this vars]
    (evaluateAndApply expressions vars divideWithInfinity))
  ;(diff [_this diffVarName]
  ;  (applyRightFoldDiff expressions diffVarName
  ;                      (fn [f g f' g']
  ;                        (_Divide.
  ;                          [(_Subtract.
  ;                             [(_Multiply. [f' g])
  ;                              (_Multiply. [g' f])])
  ;                           (_Multiply. [g g])]))
  ;                      (fn [& args] (_Divide. args))))
  )

(deftype _Negate [expression]
  Object
  (toString [_this] (expressionToString "negate" [expression]))
  IExpression
  (evaluate [_this vars]
    (- (.evaluate expression vars)))
  ;(diff [_this diffVarName]
  ;  (_Negate (.diff expression diffVarName)))
  )

(deftype _Sin [expression]
  Object
  (toString [_this] (expressionToString "sin" [expression]))
  IExpression
  (evaluate [_this vars]
    (Math/sin (.evaluate expression vars)))
  ;(diff [_this diffVarName]
  ;  (_Cos. (.diff expression diffVarName)))
  )

(deftype _Cos [expression]
  Object
  (toString [_this] (expressionToString "cos" [expression]))
  IExpression
  (evaluate [_this vars]
    (Math/cos (.evaluate expression vars)))
  ;(diff [_this diffVarName]
  ;  (_Negate. (_Sin. (.diff expression diffVarName))))
  )

(defn evaluate [expr vars] (.evaluate expr vars))
(defn diff [expr vars] (.diff expr vars))
(defn toString [expr] (.toString expr))
(defn Variable [name] (_Variable. name))
(defn Constant [value] (_Constant. value))
(defn Add [& expressions] (_Add. expressions))
(defn Subtract [& expressions] (_Subtract. expressions))
(defn Multiply [& expressions] (_Multiply. expressions))
(defn Divide [& expressions] (_Divide. expressions))
(defn Negate [expression] (_Negate. expression))
(defn Sin [expression] (_Sin. expression))
(defn Cos [expression] (_Cos. expression))

(def objectOperators {
                      '+      Add
                      '-      Subtract
                      '*      Multiply
                      '/      Divide
                      'negate Negate

                      'sin    Sin
                      'cos    Cos
                      })

(def parseObject (createParser Constant Variable objectOperators))