(ns cljs-nlp.sentiment.simple
  (:use [clojure.string :only [lower-case split]]
        [cljs.reader :only [read-string]]))

(def fs (js/require "fs"))

;; TODO: use __dirname and __filename to get correct paths
(def negative_words
  (read-string (.readFileSync fs "resources/word_lists/negative_words.edn" 
                                 "utf8")))

(def positive_words 
  (read-string (.readFileSync fs "resources/word_lists/positive_words.edn"
                                 "utf8")))

(defn collect-matches [tokens lexicon]
  (vec
   (for [token tokens
         :when (contains? lexicon token)]
     token)))

(defn rate [phrase lexicon]
  (let [tokens (split (lower-case phrase) #"\s+")
        matches (collect-matches tokens lexicon)
        hits (count matches)])
  {:score hits :comparative (/ hits (count tokens)) :words words})

(defn negative-rating [phrase]
  (rate phrase negative_words))

(defn positive-rating [phrase]
  (rate phrase positive_words))

(defn analyse [phrase]
  (let [negative (negative-rating phrase)
        positive (positive-ration phrase)]
    {:score (- (positive :score) (negative :score))
     :comparative (- (positive :comparative) (negative :comparative))
     :positive positive
     :negative negative}))
