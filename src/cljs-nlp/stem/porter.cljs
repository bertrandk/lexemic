(ns cljs-nlp.stem.porter
  (:use [clojure.string :only [lower-case replace]]))

(def step2-map {"ational" "ate"
                "tional" "tion"
                "enci" "ence"
                "anci" "ance"
                "izer" "ize"
                "bli" "ble"
                "alli" "al"
                "entli" "ent"
                "eli" "e"
                "ousli" "ous"
                "ization" "ize"
                "ation" "ate"
                "ator" "ate"
                "alism" "al"
                "iveness" "ive"
                "fulness" "ful"
                "ousness" "ous"
                "aliti" "al"
                "iviti" "ive"
                "biliti" "ble"
                "logi" "log"})

(def step3-map {"icate" "ic"
                "ative" ""
                "alize" "al"
                "iciti" "ic"
                "ical" "ic"
                "ful" ""
                "ness" ""})

(def irregular-forms {"sky" "sky"
                      "skies" "sky"
                      "dying" "die"
                      "lying" "lie"
                      "tying" "tie"
                      "news" "news"
                      "innings" "inning"
                      "inning" "inning"
                      "outings" "outing"
                      "outing" "outing"
                      "cannings" "canning"
                      "canning" "canning"
                      "howe" "howe"
                      "proceed" "proceed"
                      "exceed" "exceed"
                      "succeed" "succeed"})

(def regex
  (memoize
   (fn []
     (let [consonant "[^aeiou]"
           vowel "[aeiouy]"
           consonant-seq (str consonant "[^aeiouy]*")
           vowel-seq (str vowel "[aeiou]*")
           m>0 (str "^(" consonant-seq ")?" vowel-seq consonant-seq)
           m=0 (str "^(" consonant-seq ")?" vowel-seq consonant-seq "(" vowel-seq ")?$")
           m>1 (str "^(" consonant-seq ")?" vowel-seq consonant-seq vowel-seq consonant-seq)
           stem-vowel (str "^(" consonant-seq ")?" vowel-seq vowel)]
       {:consonant consonant
        :vowel vowel
        :consonant-seq consonant-seq
        :vowel-seq vowel-seq
        :m-greater-than-0 m>0
        :m-equals-0 m=0
        :m-greater-than-1 m>1
        :stem-vowel stem-vowel}))))

(defn normalize-word [word]
  (let [word* (lower-case word)
        first-letter (first word*)]
    (if (= first-letter "y")
      (apply str "Y" (rest word))
      word*)))

(defn step-1a [word]
  (let [re-1 #"^(.+?)(ss|i)es$"
        re-2 #"^(.+?)([^s])s$"]
    (cond
     (.test re-1 word) (replace word re-1 "$1$2")
     (.test re-2 word) (replace word re-2 "$1$2")
     :else word)))

(defn step-1b [word]
  (let [re-1 #"^(.+?)eed$"
        re-2 #"^(.+?)(ed|ing)$"
        C (:consonant-seq (regex))
        v (:vowel (regex))
        m>0 (:m-greater-than-0 (regex))
        s-v (:stem-vowel (regex))]
    (cond
     (.test re-1 word) (if (.test (re-pattern m>0) (second (first (re-seq re-1 word))))
                         (replace word #".$" "")
                         word)
     (.test re-2 word) (if (.test (re-pattern s-v) (second (first (re-seq re-2 word))))
                         (cond
                          (.test #"(at|bl|iz)$" word) (str word "e")
                          (.test #"([^aeiouylsz])\\1$" word) (replace word #".$" "")
                          (.test (re-pattern (str "^" C v "[^aeiouwxy]$")) word) (str word "e")
                          :else word)
                         word)
     :else word)))

(defn step-1c [word]
  (let [re #"^(.+?)y$"]
    (if (.test re word)
      (let [stem (second (first (re-seq re word)))
            s-v (:stem-vowel (regex))]
        (if (.test (re-pattern s-v) stem)
          (str stem "i")
          word))
    word)))

(defn step-2 [word]
  (let [re #"^(.+?)(ational|tional|enci|anci|izer|bli|alli|entli|eli|ousli|ization|ation|ator|alism|iveness|fulness|ousness|aliti|iviti|biliti|logi)$"]
    (if (.test re word)
      (let [match (first (re-seq re word))
            stem (get match 1)
            suffix (get match 2)
            m>0 (:m-greater-than-0 (regex))]
        (if (.test (re-pattern m>0) stem)
          (str stem (step2-map suffix))
          word))
      word)))

(defn step-3 [word]
  (let [re #"^(.+?)(icate|ative|alize|iciti|ical|ful|ness)$"]
    (if (.test re word)
      (let [match (first (re-seq re word))
            stem (get match 1)
            suffix (get match 2)
            m>0 (:m-greater-than-0 (regex))]
        (if (.test (re-pattern m>0) stem)
          (str stem (step3-map suffix))
          word))
      word)))

(defn step-4 [word]
  (let [re-1 #"^(.+?)(al|ance|ence|er|ic|able|ible|ant|ement|ment|ent|ou|ism|ate|iti|ous|ive|ize)$"
        re-2 #"^(.+?)(s|t)(ion)$"]
    (cond
     (.test re-1 word) (let [match (first (re-seq re-1 word))
                             stem (get match 1)
                             m>1 (:m-greater-than-1 (regex))]
                         (if (.test (re-pattern m>1) stem)
                           stem
                           word))
     (.test re-2 word) (let [match (first (re-seq re-2 word))
                             stem (str (get match 1) (get match 2))
                             m>1 (:m-greater-than-1 (regex))]
                         (if (.test (re-pattern m>1) stem)
                           stem
                           word))
     :else word)))

(defn step-5a [word]
  (let [re #"^(.+?)e$"]
    (if (.test re word)
      (let [match (first (re-seq re word))
            stem (get match 1)
            m>1 (:m-greater-than-1 (regex))
            m=1 (:m-equals-0 (regex))
            re-2 (str "^" (:consonant-seq (regex)) (:vowel (regex)) "[^aeiouwxy]$")]
        (if (and (or (.test (re-pattern m>1) stem)
                     (.test (re-pattern m=1) stem))
                 (not (.test (re-pattern re-2) stem)))
          stem
          word))
      word)))

(defn step-5b [word]
  (let [re #"ll$"
        m>1 (:m-greater-than-1 (regex))]
    (if (and (.test re word) (.test (re-pattern m>1) word))
      (replace word #".$" "")
      word)))

(defn reset-Y [word]
  (let [first-letter (first word)]
    (if (= first-letter "Y")
      (apply str "y" (rest word))
      word)))

(defn perform-steps [word]
  (-> word
      (normalize-word)
      (step-1a)
      (step-1b)
      (step-1c)
      (step-2)
      (step-3)
      (step-4)
      (step-5a)
      (step-5b)
      (reset-Y)))

(defn stem [word]
  (cond
   (contains? irregular-forms word) (irregular-forms word)
   (< (count word) 3) word
   :else (perform-steps word)))