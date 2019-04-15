# re-demo

A [re-frame](https://github.com/Day8/re-frame) application designed to
... well, that part  is up to you. The project  was generated from the
[template](https://github.com/Day8/re-frame-template).  Later the most
of the effect of option "+handler" was manually added.

## Development Mode

### Run application:

    lein clean
    lein figwheel dev

Figwheel will automatically push cljs  changes to the browser.  Wait a
bit,  then  browse to  [http://localhost:3449](http://localhost:3449).
FIXME: this  looks like the Figwheel  server which is not  the same as
the full stack handeler of the production build.

## Production Build

To compile clojurescript to javascript:

    lein clean
    lein cljsbuild once min

Full stack build:

    lein clean
    lein with-profile prod uberjar

That should compile the clojurescript  code first, and then create the
standalone jar.

When you run the jar you can set  the port the ring server will use by
setting the environment  variable PORT.  If it's not set,  it will run
on port 3000 by default:

    port=3449 java -jar ./target/re-demo.jar
