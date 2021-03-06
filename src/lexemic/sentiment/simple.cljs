(ns lexemic.sentiment.simple
  (:use [clojure.string :only [lower-case split]])
  (:require [lexemic.data.sentiment :as data]))

(defn- collect-matches [tokens lexicon]
  (for [token tokens
        :when (contains? lexicon token)]
    token))

(defn- rate [phrase lexicon]
  (let [tokens (split (lower-case phrase) #"\s+")
        matches (collect-matches tokens lexicon)
        hits (count matches)]
    {:score hits :comparative (/ hits (count tokens)) :words matches}))

(defn negative-rating [phrase]
  (rate phrase data/negative-words))

(defn positive-rating [phrase]
  (rate phrase data/positive-words))

(defn analyse [phrase]
  (let [negative (negative-rating phrase)
        positive (positive-rating phrase)]
    {:score (- (get positive :score) (get negative :score))
     :comparative (- (get positive :comparative) (get negative :comparative))
     :positive positive
     :negative negative}))
