(ns lexemic.stats.levenshtein
  (:use [clojure.string :only [blanks?]]))

(defn create-element
  [char1 char2 previous-row current-row idx]
  (if (= char1 char2)
    (previous-row (- idx 1))
    (+ 1 (min
      (previous-row (- idx 1))
      (previous-row idx)
      (last current-row)))))
 
(defn create-row
  [char1 str2 previous-row current-row]
  (let [char2 (first str2)
        idx (count current-row)]
    (if (= (count current-row) (count previous-row))
      current-row
      (recur
        char1
        (rest str2)
        previous-row
        (conj current-row (create-element char1 char2 previous-row current-row idx))))))

(defn levenshtein
  "Calculate the edit distance between two strings."
  ([str1 str2]
    (let [initial-row (vec (range (inc (count str2))))]
      (levenshtein 1 initial-row str1 str2)))
  ([row-no previous-row str1 str2]
    (let [next-row (create-row (first str1) str2 previous-row [row-no])
          rest-str1 (subs str1 1)]
      (if (blank? rest-str1)
        (last next-row)
        (recur (inc row-no) next-row rest-str1 str2)))))
