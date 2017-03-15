(set-env!
 :source-paths    #{"src/cljs"}
 :resource-paths  #{"resources"}
 :dependencies '[[adzerk/boot-cljs              "1.7.228-2"  :scope "test"]
                 [adzerk/boot-cljs-repl         "0.3.3"      :scope "test"]
                 [adzerk/boot-reload            "0.4.13"     :scope "test"]
                 [pandeiro/boot-http            "0.7.6"      :scope "test"]
                 [afrey/boot-asset-fingerprint  "1.2.0"      :scope "test"]
                 [com.cemerick/piggieback       "0.2.1"      :scope "test"]
                 [org.clojure/tools.nrepl       "0.2.12"     :scope "test"]
                 [weasel                        "0.7.0"      :scope "test"]
                 [crisptrutski/boot-cljs-test   "0.3.0"      :scope "test"]
                 [binaryage/dirac               "1.1.3"      :scope "test"]
                 [powerlaces/boot-cljs-devtools "0.2.0"      :scope "test"]
                 [org.clojure/clojurescript     "1.9.293"]
                 [reagent                       "0.6.0"]
                 [cljsjs/tonejs                 "0.8.0-1"]])

(require
 '[adzerk.boot-cljs      :refer [cljs]]
 '[adzerk.boot-cljs-repl :refer [cljs-repl cljs-repl-env start-repl]]
 '[adzerk.boot-reload    :refer [reload]]
 '[pandeiro.boot-http    :refer [serve]]
 '[afrey.boot-asset-fingerprint :refer [asset-fingerprint]]
 '[crisptrutski.boot-cljs-test :refer [test-cljs]]
 '[powerlaces.boot-cljs-devtools :refer [cljs-devtools dirac]])

(deftask build []
  (comp (speak)
        (cljs)))

(deftask run []
  (comp (serve)
        (watch)
        (cljs-repl-env)

        (dirac)
        (reload)
        (build)))

(deftask production []
  (task-options! cljs {:optimizations :advanced})
  identity)

(deftask development []
  (task-options! cljs {:optimizations :none}
                 reload {:on-jsload 'trumpet.app/init})
  identity)

(deftask dev
  "Simple alias to run application in development mode"
  []
  (comp (development)
        (run)))

(deftask prod
  []
  (comp (production)
        (build)
        (asset-fingerprint)))

(deftask testing []
  (set-env! :source-paths #(conj % "test/cljs"))
  identity)

;;; This prevents a name collision WARNING between the test task and
;;; clojure.core/test, a function that nobody really uses or cares
;;; about.
(ns-unmap 'boot.user 'test)

(deftask test []
  (comp (testing)
        (test-cljs :js-env :phantom
                   :exit?  true)))

(deftask auto-test []
  (comp (testing)
        (watch)
        (test-cljs :js-env :phantom)))
