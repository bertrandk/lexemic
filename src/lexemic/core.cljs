(ns lexemic.core
  (:use [clojure.string :only [split]])
  (:require [cljs.nodejs :as node]
            [lexemic.sentiment.simple :as sentiment]
            [lexemic.stem.lancaster :as lancaster]
            [lexemic.stem.porter :as porter]
            [lexemic.tokenize.treebank :as treebank]))

(def ^:private version
  (.-version (node/require (str js/__dirname "/../package.json"))))
(def ^:private fs
  (node/require "fs"))

(def ^:private spacer "")
(def ^:private version (str "Lexemic v" version))
(def ^:private usage-banner "Usage: lexemic [command] [implementation] [target...]")

(defn- show-help []
  (do
    (println version)
    (println spacer)
    (println usage-banner)))

(defn- run [cmd impl text]
  (condp = cmd
    "sentiment" (prn (sentiment/analyse (print-str text)))
    "stem" (let [words (split (apply str text) #"\s+")]
             (condp contains? impl
               #{"-lancaster" "-l"} (prn (set (map lancaster/stem words)))
               #{"-porter" "-p"} (prn (set (map porter/stem words)))
               (prn (set (map porter/stem words)))))
    "tokenize" (let [words (apply str text)]
                 (prn (treebank/tokenize words)))
    (show-help)))

(defn -main [& args]
  (let [command (first args)
        impl (let [impl* (second args)]
               (when (and impl* (.match impl* #"^-{1,2}"))
                 impl*))
        targets (if impl
                  (drop 2 args)
                  (drop 1 args))
        file? (fn [path]
                (.existsSync fs path))
        get-string (fn [x]
                     (if (file? x)
                       (.readFileSync fs x "utf8")
                       (identity x)))
        input (map get-string targets)]
    (if command
      (run command impl input)
      (show-help))))

(set! *main-cli-fn* -main)
