(ns foo.droid.main
  (:use [neko.activity :only [defactivity set-content-view!]]
        [neko.threading :only [on-ui post]]
        [neko.ui :only [make-ui]]
        [neko.notify :as n :only [toast notification]]
        [neko.log :as log]
        [neko.ui.traits :only [deftrait]]
        [neko.ui.mapping :only [add-trait!]])
  (:import com.google.android.gms.common.GooglePlayServicesUtil
           com.google.android.gms.gcm.GoogleCloudMessaging
           android.view.View
           android.os.AsyncTask
           java.lang.Thread))

(defn check-google-play-services [context]
  (GooglePlayServicesUtil/isGooglePlayServicesAvailable context))

(def sender-id "617220181631")

(defn on-background [f s]
  (let [task (proxy [AsyncTask] []
                (doInBackground [_]
                  (f))
                (onPostExecute [res]
                  (s res)))]
    (.execute task (into-array Object []))))

(def regid (atom nil))

(defn register [context]
  (let [gcm (GoogleCloudMessaging/getInstance context)]
    (.register gcm (into-array String [sender-id]))))

(deftrait :ref-text [^android.widget.TextView wdg {:keys [ref-text]}]
  (add-watch ref-text nil
             (fn [_ _ o n] (post wdg (.setText wdg n)))))

(add-trait! :edit-text :ref-text)

(declare ^android.widget.EditText et)

(def ui [:linear-layout {:id-holder true}
         [:text-view {:text "Hello from Clojure!!!"}]
         [:edit-text {:text "ugg" :ref-text regid}]])

(defn register-in-background [context]
  (on-background
   (fn [] (register context))
   (fn [r]
     (reset! regid r))))

(defactivity foo.droid.MainActivity
  :def a
  :on-create
  (fn [this bundle]
    ;(register-in-background this)
    (on-ui
     (set-content-view! a (make-ui ui)))))
