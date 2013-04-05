(ns cljs-nlp.tokenize.treebank
  (:use [clojure.string :only [split replace trim]]))

(defn replace-starting-quotes [s]
  (-> s
      (replace #"^\"" "``")
      (replace #"(``)" " $1 ")
      (replace #"([ (\[{<])\"" "$1 `` ")))

(defn replace-punctuations [s]
  (-> s
      (replace #"([:,])([^\d])" " $1 $2")
      (replace #"\.\.\." " ... ")
      (replace #"[;@#$%&]" " $& ")
      (replace #"([^\.])(\.)([\]\)}>\"\']*)\s*$" "$1 $2$3 ")
      (replace #"[?!]" " $& ")
      (replace #"([^'])' " "$1 ' ")
      
      (replace #"[\]\[\(\)\{\}\<\>]" " $& ")
      (replace #"--" " -- ")))

(defn pad-string [s]
  (str " " s " "))

(defn replace-ending-quotes [s]
  (-> s
      (replace #"\"" " '' ")
      (replace #"(\S)(\'\')" "$1 $2 ")
      (replace #"([^' ])('[sS]|'[mM]|'[dD]|') " "$1 $2 ")
      (replace #"([^' ])('ll|'re|'ve|n't|) " "$1 $2 ")
      (replace #"([^' ])('LL|'RE|'VE|N'T|) " "$1 $2 ")))

(defn replace-contractions [s]
  (-> s
      (replace #"(?i)\b(can)(not)\b" " $1 $2 ")
      (replace #"(?i)\b(d)('ye)\b" " $1 $2 ")
      (replace #"(?i)\b(gim)(me)\b" " $1 $2 ")
      (replace #"(?i)\b(gon)(na)\b" " $1 $2 ")
      (replace #"(?i)\b(got)(ta)\b" " $1 $2 ")
      (replace #"(?i)\b(lem)(me)\b" " $1 $2 ")
      (replace #"(?i)\b(mor)('n)\b" " $1 $2 ")
      (replace #"(?i)\b(wan)(na) " " $1 $2 ")
      (replace #"(?i) ('t)(is)\b" " $1 $2 ")
      (replace #"(?i) ('t)(was)\b" " $1 $2 ")))


(defn tokenize [s]
  (-> s
      (replace-starting-quotes)
      (replace-punctuations)
      (pad-string)
      (replace-ending-quotes)
      (replace-contractions)
      (trim)
      (split #"\s+")))