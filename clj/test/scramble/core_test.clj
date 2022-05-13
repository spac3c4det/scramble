(ns scramble.core-test
  (:require [clojure.test :refer [deftest testing is use-fixtures]]
            [scramble.core :refer [scramble? index-str]]))

(deftest scramble?-test
  (testing "Tests the scramble? function against cases given in the assignment"
    (is (= (scramble? "rekqodlw" "world")
           true))
    (is (= (scramble? "cedewaraaossoqqyt" "codewars")
           true))
    (is (= (scramble? "katas" "steak")
           false))))

(deftest index-test
  (testing "Tests the string indexing function")
  (is (= (index-str "rekqodlw" {\r 1
                                \e 1
                                \k 1
                                \q 1
                                \o 1
                                \d 1
                                \l 1
                                \w 1})))
  (is (= (index-str "katas")
         {\k 1 \a 2 \t 1 \s 1})))
