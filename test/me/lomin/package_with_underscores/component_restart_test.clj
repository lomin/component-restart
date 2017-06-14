(ns me.lomin.package-with-underscores.component-restart-test
  (:require [clojure.test :refer :all]
            [me.lomin.package-with-underscores.some-test :as st]
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


(deftest watching
  (testing "call event-fn"
    (let [called (atom false)
          event-fn (fn [] (reset! called true))
          some-test "test/me/lomin/package_with_underscores/some_test.clj"
          original-content (slurp some-test)]
      (with-redefs [cr/reload (constantly nil)]
        (try
          (let [f (future
                    (dotimes [n 10]
                      (Thread/sleep 100)
                      (spit some-test "(ns me.lomin.package-with-underscores.some-test)\n\n\n(defn somefoo [] :foo)")))]
            (cr/watch (var st/somefoo) event-fn)
            (is (= true @called)))
          (finally
            (spit some-test original-content)))))))