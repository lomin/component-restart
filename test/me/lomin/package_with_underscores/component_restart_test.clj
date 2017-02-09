(ns me.lomin.package-with-underscores.component-restart-test
  (:require [clojure.test :refer :all]
            [me.lomin.component-restart :as cr]))

(defn constant-three []
  3)

(deftest resolve-callback-test
  (let [a-meta (meta (var constant-three))]
    (is (= "/me/lomin/package_with_underscores/component_restart_test"
           (cr/ns-file-name a-meta)))
    (is (= (symbol "me.lomin.package-with-underscores.component-restart-test")
           (cr/ns-symbol a-meta)))
    (is (= (var constant-three)
           (cr/resolve-callback (meta (var constant-three)))))))