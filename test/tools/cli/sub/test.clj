(ns tools.cli.sub.test
  (:require [tools.cli.sub.core :as cli-sub]))

(def ^:dynamic size-opt
  [nil "--size SIZE" "result size"])

(def commands
  ; The "root" command should be entered in ": __ root__".
  {:__root__ {:description {:arguments "[options] action"
                            :usage     "Main usage..."}}

   :file     {:options     []
              :description {:arguments "[sub command]"
                            :usage     "file commands"}
              :sub-command {:ls   {:options     [size-opt]
                                   :description {:arguments "[options...]"
                                                 :usage     "Show file list"}
                                   :handler     (fn [arguments options]
                                                  ;; Do something...
                                                  )}
                            :find {:options              [size-opt]
                                   :description          {:arguments "[FILE NAME]"
                                                          :usage     "find matched files"}
                                   :pass-if-no-arguments true
                                   :handler              (fn [arguments options]
                                                           ;; Do something...
                                                           (cond
                                                             (empty? arguments) (throw (IllegalArgumentException. "File name is requried!"))
                                                             :else (println "Done."))
                                                           )}}}

   :find     {:alias [:file :find]}})




(cli-sub/parser commands "")

(cli-sub/parser commands "file")
(cli-sub/parser commands "file" "find")
(cli-sub/parser commands "file" "find" "hello_clojure.clj" "--size" "10")

(cli-sub/parser commands "find")
(cli-sub/parser commands "find" "-h")



(def handlers {:help   (fn [usage]
                         (println usage))
               :errors (fn [errors]
                         (println errors))})

(cli-sub/parser-with-supervisor commands handlers
                                "")

(cli-sub/parser-with-supervisor commands handlers
                                "file")

(cli-sub/parser-with-supervisor commands handlers
                                "file" "find")

(cli-sub/parser-with-supervisor commands handlers
                                "file" "find" "hello_clojure.clj")

(cli-sub/parser-with-supervisor commands handlers
                                "find")

(cli-sub/parser-with-supervisor commands handlers
                                "find" "-h")