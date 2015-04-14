# component-restart

Provides a single function that watches for changes in source files in all directories on the classpath. When a change has been detected, it reloads the modifed source files and restarts the component-system.

## Leiningen

*component-restart* is available from Clojars.  Add the following dependency to your *project.clj*:
[![Clojars Project](http://clojars.org/me.lomin/component-restart/latest-version.svg)](http://clojars.org/me.lomin/component-restart)

## Usage ##

Call the watch function with two parameters: the var pointing to the function to restart and the started system.

```clojure
(require '[me.lomin.component-restart :as restart])
(defn -main []
  (let [system (component/start (make-system))]
    (restart/watch (var -main) system)))
```

## License ##

    Copyright © 2015 Steven Collins. All rights reserved. The use and
    distribution terms for this software are covered by the Eclipse
    Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
    which can be found in the file LICENSE at the root of this
    distribution. By using this software in any fashion, you are
    agreeing to be bound by the terms of this license. You must
    not remove this notice, or any other, from this software.
