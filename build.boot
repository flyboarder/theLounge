(set-env!
  :dependencies  '[[clj-tagsoup               "0.3.0"]
                   [adzerk/boot-cljs-repl     "0.1.9"]
                   [adzerk/boot-reload        "0.2.6"]
                   [pandeiro/boot-http        "0.6.2"]
                   [markdown-clj              "0.9.63"]
                   [adzerk/boot-cljs          "0.0-2814-0"]
                   [tailrecursion/boot-hoplon "0.1.0-SNAPSHOT"]
                   [tailrecursion/hoplon      "6.0.0-SNAPSHOT"]]
  :source-paths   #{"src"}
  :resource-paths #{"resources/assets"}
  :target-path    "resources/public")

(require
  '[adzerk.boot-cljs :refer [cljs]]
  '[adzerk.boot-cljs-repl :refer [cljs-repl start-repl]]
  '[adzerk.boot-reload :refer [reload]]
  '[pandeiro.boot-http :refer [serve]]
  '[tailrecursion.boot-hoplon :refer [haml hoplon prerender html2cljs]])

(swap! boot.repl/*default-dependencies*
       concat '[[lein-light-nrepl "0.1.0"]
                [org.clojure/clojurescript "0.0-3308"]])

(swap! boot.repl/*default-middleware*
       conj 'lighttable.nrepl.handler/lighttable-ops)

(deftask dev
  "Build hoplon.io for local development."
  []
  (comp
    (watch)
    (hoplon :pretty-print true)
    (cljs :optimizations :none
          :source-map    true)
    (serve :dir (get-env :target-path))
    (speak)))

(deftask prod
  "Build hoplon.io for production deployment."
  []
  (comp
    (hoplon :pretty-print true)
    (cljs :optimizations :advanced :source-map true)
    (prerender)))