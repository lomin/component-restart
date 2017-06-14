(ns me.lomin.component-restart
  (:require [clojure.string :as string]
            [clojure.tools.logging :as log]
            [clojure.tools.namespace.dir :as dir]
            [clojure.tools.namespace.repl :as repl]
            [clojure.tools.namespace.track :as track]))

(defn- to-relative-resource [a-meta]
  (string/join "." (pop (string/split (:file a-meta) #"\."))))

(defn ns-file-name [a-meta]
  (str "/" (to-relative-resource a-meta)))

(defn ns-symbol [a-meta]
  (-> (to-relative-resource a-meta)
      (string/replace #"_" "-")
      (string/replace #"/" ".")
      (symbol)))

(defn load-clojure-file [file-name]
  (log/debug (str "trying to load <" file-name ">"))
  (load file-name)
  (log/debug "file successfully loaded."))

(defn resolve-callback [a-meta]
  (let [ns-sym (ns-symbol a-meta)
        f-sym (:name a-meta)]
    (log/debug (str "starting callback <" ns-sym "><" f-sym ">"))
    (ns-resolve ns-sym f-sym)))

(defn reload [a-var]
  (let [a-meta (meta a-var)
        file-name (ns-file-name a-meta)]
    (repl/refresh)
    (load-clojure-file file-name)
    ((resolve-callback a-meta))))

(defn watch
  ([callback-var event-fn]
   (watch callback-var event-fn (dir/scan (track/tracker))))
  ([callback-var event-fn tracker]
  (let [new-tracker (dir/scan tracker)]
    (if (not= new-tracker tracker)
      (do
        (event-fn)
        (reload callback-var))
      (do (Thread/sleep 200)
        (recur callback-var event-fn new-tracker))))))
