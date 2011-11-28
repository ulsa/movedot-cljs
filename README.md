# movedot-cljs

A simple "move the ball" example that has been translated from Google Closure into ClojureScript. The purpose is to
show how to write ClojureScript applications that interact with the keyboard and the browser canvas.

Inspiration from [teebes.com](http://teebes.com/blog/19/playing-with-googles-closure-js-library).

The state is kept in a single atom called `dot`, which is a map
containing the x and y coordinates of the ball, plus the graphics of
the ball. Hitting an arrow key will change the state of the x/y
coordinates, and then change the center of the graphics of the ball
accordingly. This will effectively redraw the ball in its new position.

## Usage

1. Compile `src`
1. Open `movedot_optimized.html` (or `movedot.html`) in browser

### Pre-requisites

Get ClojureScript by following the instructions on the [Getting Started](https://github.com/clojure/clojurescript/wiki/Quick-Start)
page.

### Production mode

Compile with `:advanced` level:

    % cljsc src '{:optimizations :advanced}' > movedot.js

Results in a 72K JavaScript file and nothing more. Pretty impressive. Open `movedot_optimized.html`, not `movedot.html`.

#### Other optimizations levels

There are two other levels of optimizations, which can be handy, either if there is a problem with the `:advanced` level,
or if you actually want to read the compiled code. Code compiled using `:advanced` level is obfuscated and unreadable.

The first is the `:simple` level, which results in a single 430K JavaScript file, which is actually almost readable due
to the `:pretty-print` options (which you can remove if you want). Some symbols will be renamed, but only those that are
local within functions or expressions.

    % cljsc src '{:optimizations :simple :pretty-print true}' > movedot.js

The `:whitespace` level removes redundant things like whitespace, linebreaks, and comments, but performs no renaming of
symbols. It results in a single 600K JavaScript file. Skip the `:pretty-print` option if you don't need it.

    % cljsc src '{:optimizations :whitespace :pretty-print true}' > movedot.js

### Development mode

Development mode means pretty much "don't use any optimizations". It means that all the Google Closure files and the
required ClojureScript files will be available in the `out` folder. The `movedot.js` file will only contain references
to other files. This mode simplifies debugging, but requires a few more lines in the HTML file, so you must use the
`movedot.html` file when using this mode.

You can choose between compiling from the command-line, from the REPL, or using a 'watcher' that compiles automatically
as soon as any watched file has changed. I recommend the watcher approach, but if you're new to ClojureScript, you might
want to compile manually and check the results in the `out` folder and the `moveto.js` file.

#### Compile from the command-line (easy)

Easy to do, but incurs the overhead of starting the JVM each time you compile.

    % cljsc src > movedot.js

#### Compile from the REPL (fast)

Starts the JVM only once, when the REPL is started, and compiles pretty quickly after that (about 50 ms on my
machine). However, you must manually trigger a re-compile. You'll want command-line editing in the REPL, though.
That can be achieved either using `rlwrap` or JLine. Both options are described later.

    % $CLOJURESCRIPT_HOME/script/repl
    user=> (require '[cljs.closure :as cljsc])
    user=> (cljsc/build "src" {:output-dir "out" :output-to "movedot.js"})

##### rlwrap

1. Install `rlwrap` using your regular package manager, like `brew install rlwrap`.
1. Start the REPL using rlwrap:

        % rlwrap $CLOJURESCRIPT_HOME/script/repl

##### JLine

1. [Download JLine](http://jline.sourceforge.net/downloads.html)
1. Place the JLine jar in `$CLOJURESCRIPT_HOME/lib`
1. Change `$CLOJURESCRIPT_HOME/script/repl` slightly, by adding the `jline.ConsoleRunner` before `clojure.main`:

    ```diff
    -java -server -cp $CLJSC_CP clojure.main
    +java -server -cp $CLJSC_CP jline.ConsoleRunner clojure.main
    ```

#### Compile automatically (easy and convenient)

Note that the watcher script uses `:simple` optimizations and `:pretty-print` by default. We can disable optimizations
by passing `nil`.

    % cljs-watch src '{:optimizations nil :output-dir "out" :output-to "movedot.js"}'
    21:09:19 :: watcher :: Building ClojureScript files in :: src     [done]
    21:09:26 :: watcher :: Waiting for changes

You get the watcher by doing this:

    % git clone https://github.com/ibdknox/cljs-watch.git
    % cp cljs-watch/cljs-watch /usr/local/bin

## License

Copyright (C) 2011 Ulrik Sandberg, Jayway

Distributed under the Eclipse Public License, the same as Clojure.
