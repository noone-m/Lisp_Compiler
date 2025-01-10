;(defparameter x (- 3 5))
;(write x)
;(WRITE-LINe x)
;(defparameter x2 (or NIL NIL NIL NIL))
;(write x2)
;(defparameter x3 (not T))
;(write x3)
;(defparameter x4 (= 8 8  4))
;(write x4)

;(<=  5 3 6)
;(>=  5 3 6)
;( = 5 3 6)

;(defparameter age 20)
;(if (> age 18) (write "accepted"))

;(defparameter a 133)
;(if (> a 20)
;(write "a is bigger than 20") (write " a is less than 20"))


;(setq x 2)
;(cond ((> x 20) (write "greater than twenty"))
;((< x 5) (write "less than five"))
;(t ( write "between-five-and-twenty") (write "way to go") (defparameter way 10)))


;(defun add-two-numbers (a b)
;  (+ a b))
;(write (add-two-numbers -3 4))
;(write (add-two-numbers -1 4))

(defparameter name "mahdi")
(format t "hello~%" )
(format t "hello ~a" name)


(defun hello_world () (format t "~%hello world") )
(hello_world)