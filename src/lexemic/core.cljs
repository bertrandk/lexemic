(ns lexemic.core
  (:require [cljs.nodejs :as node]
            [lexemic.sentiment.simple :as sentiment]))

(def ^:private fs
  (node/require "fs"))

(def ^:private usage-banner "Usage: lexemic [command] [implementation] [target...]")
(def ^:private supported-commands #{"help" "sentiment"})

(defn- help []
  (str usage-banner))

(defn- run [cmd impl text]
  (condp = cmd
    "sentiment" (sentiment/analyse text)))

(defn -main [& args]
  (let [command (first args)
        impl (when (.match (second args) #"^-{1,2}")
               (second args))
        targets (if impl
                  (drop 2 args)
                  (drop 1 args))
        file? (fn [path]
                (.existsSync fs path))
        get-string (fn [x]
                     (if (file? x)
                       (.readFileSync fs x "utf8")
                       (identity x)))
        contents (map get-string targets)
        input (print-str contents)]
    (if-let [cmd (supported-commands command)]
      (println (run cmd impl input))
      (println (help)))))

(set! *main-cli-fn* -main)
