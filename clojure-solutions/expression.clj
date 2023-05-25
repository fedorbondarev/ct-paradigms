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
  (^String toStringPostfix [])
  ;(^Number diff [diffVarName])
  )

(deftype _Constant [value]
  Object
  (toString [_this] (str value))
  IExpression
  (evaluate [_this _vars] value)
  (toStringPostfix [this] (.toString this))
  ;(diff [_this _diffVarName] (_Constant. 0))
  )

(deftype _Variable [name]
  Object
  (toString [_this] name)
  IExpression
  (evaluate [_this vars]
    (get
      vars
      (clojure.string/lower-case
        (str (get name 0)))))
  (toStringPostfix [this] (.toString this))
  ;(diff [_this diffVarName]
  ;  (if (= diffVarName name)
  ;    (_Constant. 1)
  ;    (_Constant. 0)))
  )

(declare toString)
(declare toStringPostfix)

(defn evaluateAndApply [expressions vars fun]
  (apply fun
         (mapv #(.evaluate % vars) expressions)))

(defn exprListToStr [exprs elementToStr]
  (clojure.string/join " "
                       (mapv elementToStr exprs)))

(defn expressionToString [operator children]
  (str "(" operator " "
       (exprListToStr children toString)
       ")"))

(defn expressionToStringPostfix [operator children]
  (str "("
       (exprListToStr children toStringPostfix)
       " " operator ")"))

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

(def _addSymbol "+")
(deftype _Add [expressions]
  Object
  (toString [_this] (expressionToString _addSymbol expressions))
  IExpression
  (evaluate [_this vars]
    (evaluateAndApply expressions vars +))
  (toStringPostfix [_this] (expressionToStringPostfix _addSymbol expressions))
  ;(diff [_this diffVarName]
  ;  (applyDiff expressions diffVarName
  ;             (fn [_exprs diffExprs] (_Add. diffExprs))))
  )

(def _subtractSymbol "-")
(deftype _Subtract [expressions]
  Object
  (toString [_this] (expressionToString _subtractSymbol expressions))
  IExpression
  (evaluate [_this vars]
    (evaluateAndApply expressions vars -))
  (toStringPostfix [_this] (expressionToStringPostfix _subtractSymbol expressions))
  ;(diff [_this diffVarName]
  ;  (applyDiff expressions diffVarName
  ;             (fn [_exprs diffExprs] (_Subtract. diffExprs))))
  )

(def _multiplySymbol "*")
(deftype _Multiply [expressions]
  Object
  (toString [_this] (expressionToString _multiplySymbol expressions))
  IExpression
  (evaluate [_this vars]
    (evaluateAndApply expressions vars *))
  (toStringPostfix [_this] (expressionToStringPostfix _multiplySymbol expressions))
  ;(diff [_this diffVarName]
  ;  (applyRightFoldDiff expressions diffVarName
  ;                      (fn [f g f' g']
  ;                        (_Add.
  ;                          [(_Multiply. [f' g])
  ;                           (_Multiply. [g' f])]))
  ;                      (fn [& args] (_Multiply. args))))
  )

(def _divideSymbol "/")
(deftype _Divide [expressions]
  Object
  (toString [_this] (expressionToString _divideSymbol expressions))
  IExpression
  (evaluate [_this vars]
    (evaluateAndApply expressions vars divideWithInfinity))
  (toStringPostfix [_this] (expressionToStringPostfix _divideSymbol expressions))
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

(def _negateSymbol "negate")
(deftype _Negate [expression]
  Object
  (toString [_this] (expressionToString _negateSymbol [expression]))
  IExpression
  (evaluate [_this vars]
    (- (.evaluate expression vars)))
  (toStringPostfix [_this] (expressionToStringPostfix _negateSymbol [expression]))
  ;(diff [_this diffVarName]
  ;  (_Negate (.diff expression diffVarName)))
  )

(def _sinSymbol "sin")
(deftype _Sin [expression]
  Object
  (toString [_this] (expressionToString _sinSymbol [expression]))
  IExpression
  (evaluate [_this vars]
    (Math/sin (.evaluate expression vars)))
  (toStringPostfix [_this] (expressionToStringPostfix _sinSymbol [expression]))
  ;(diff [_this diffVarName]
  ;  (_Cos. (.diff expression diffVarName)))
  )

(def _cosSymbol "cos")
(deftype _Cos [expression]
  Object
  (toString [_this] (expressionToString _cosSymbol [expression]))
  IExpression
  (evaluate [_this vars]
    (Math/cos (.evaluate expression vars)))
  (toStringPostfix [_this] (expressionToStringPostfix _cosSymbol [expression]))
  ;(diff [_this diffVarName]
  ;  (_Negate. (_Sin. (.diff expression diffVarName))))
  )

(def _incSymbol "++")
(deftype _Inc [expression]
  Object
  (toString [_this] (expressionToString _incSymbol [expression]))
  IExpression
  (evaluate [_this vars]
    (+ (.evaluate expression vars) 1))
  (toStringPostfix [_this] (expressionToStringPostfix _incSymbol [expression]))
  ;(diff [_this diffVarName]
  ;  (_Cos. (.diff expression diffVarName)))
  )

(def _decSymbol "--")
(deftype _Dec [expression]
  Object
  (toString [_this] (expressionToString _decSymbol [expression]))
  IExpression
  (evaluate [_this vars]
    (- (.evaluate expression vars) 1))
  (toStringPostfix [_this] (expressionToStringPostfix _decSymbol [expression]))
  ;(diff [_this diffVarName]
  ;  (_Negate. (_Sin. (.diff expression diffVarName))))
  )

(defn evaluate [expr vars] (.evaluate expr vars))
(defn diff [expr vars] (.diff expr vars))
(defn toString [expr] (.toString expr))
(defn toStringPostfix [expr] (.toStringPostfix expr))
(defn Variable [name] (_Variable. name))
(defn Constant [value] (_Constant. value))
(defn Add [& expressions] (_Add. expressions))
(defn Subtract [& expressions] (_Subtract. expressions))
(defn Multiply [& expressions] (_Multiply. expressions))
(defn Divide [& expressions] (_Divide. expressions))
(defn Negate [expression] (_Negate. expression))
(defn Sin [expression] (_Sin. expression))
(defn Cos [expression] (_Cos. expression))
(defn Inc [expression] (_Inc. expression))
(defn Dec [expression] (_Dec. expression))

(def objectOperators {
                      '+      Add
                      '-      Subtract
                      '*      Multiply
                      '/      Divide
                      'negate Negate

                      'sin    Sin
                      'cos    Cos

                      '++     Inc
                      '--     Dec
                      })

(def parseObject (createParser Constant Variable objectOperators))

(load-file "parser.clj")

(declare get*expr)

(def operatorsKeys (sort-by (comp - count) (mapv str (keys objectOperators))))

(defn sign [c tail]
  (if (#{\- \+} c)
    (cons c tail)
    tail))

(def *digitsStr (+str (+plus (+char "0123456789"))))

(def *number
  (+map
    (comp Constant read-string)
    (+str
      (+seqf
        sign
        (+opt (+char "+-"))
        (+map
          #(if (nil? (second %)) (first %) (apply str %))
          (+seq
            *digitsStr
            (+opt
              (+str (+seq
                      (+char ".")
                      *digitsStr)))))))))

(def *variable
  (+map
    Variable
    (+str (+plus (+char "xyzXYZ")))))

(defn +string [s]
  (+map
    (partial apply str)
    (apply +seq
           (mapv (comp +char str) s))))

(def *operator
  (+map
    #((symbol %) objectOperators)
    (apply
      +or
      (mapv
        +string
        operatorsKeys))))

(def *space (+char " \t\n\r"))
(def *ws (+ignore (+star *space)))

(def *complexExpr
  (+map #(apply (second %) (first %))
        (+seq
          (+ignore (+char "("))
          (+star (delay (get*expr)))
          *operator
          *ws
          (+ignore (+char ")"))
          )))

(def *expr
  (+map
    first
    (+seq
      *ws
      (+or *number *variable *complexExpr)
      *ws)))

(defn get*expr [] *expr)

(def parseObjectPostfix (+parser *expr))
