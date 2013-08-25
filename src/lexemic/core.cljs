(ns lexemic.core
  (:require [cljs.nodejs :as node]
            [lexemic.sentiment.simple :as sentiment]))

(def ^:private fs
  (node/require "fs"))

(def ^:private usage-banner "Usage: lexemic [command] [target]")
(def ^:private supported-commands #{"help" "sentiment"})

(defn- help []
  (str usage-banner))

(defn- run [cmd text]
  (condp = cmd
    "sentiment" (sentiment/analyse text)))

(defn -main [& args]
  (let [command (first args)
        target (second args)
        input (if (.existsSync fs target)
                (.readFileSync fs target "utf8")
                target)]
    (if-let [cmd (supported-commands command)]
      (println (run cmd input))
      (println (help)))))

(set! *main-cli-fn* -main)
