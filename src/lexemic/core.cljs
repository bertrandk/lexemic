(ns lexemic.core
  (:require [lexemic.sentiment.simple :as sentiment]))

(def ^:private usage-banner "Usage: lexemic [command] [target]")
(def ^:private supported-commands #{"help" "sentiment"})

(defn- show-help []
  (do
    (println usage-banner)))

(defn- run [cmd text]
  (condp = cmd
    "sentiment" (println (sentiment/analyse text))))

(defn -main [& args]
  (let [command (first args)
        input (second args)]
    (if-let [cmd (supported-commands command)]
      (run cmd input)
      (show-help))))

(set! *main-cli-fn* -main)
