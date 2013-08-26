(ns lexemic.core
  (:require [cljs.nodejs :as node]
            [lexemic.sentiment.simple :as sentiment]))

(def ^:private version
  (.-version (node/require (str (.cwd js/process) "/package.json"))))
(def ^:private fs
  (node/require "fs"))

(def ^:private spacer "")
(def ^:private version (str "Lexemic v" version))
(def ^:private usage-banner "Usage: lexemic [command] [implementation] [target...]")
(def ^:private supported-commands #{"help" "sentiment"})

(defn- show-help []
  (do
    (println version)
    (println spacer)
    (println usage-banner)))

(defn- run [cmd impl text]
  (condp = cmd
    "sentiment" (prn (sentiment/analyse text))
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
        contents (map get-string targets)
        input (print-str contents)]
    (if-let [cmd (supported-commands command)]
      (run cmd impl input)
      (show-help))))

(set! *main-cli-fn* -main)
