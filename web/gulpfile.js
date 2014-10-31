var browserify = require('browserify');
var del = require('del');
var gulp = require('gulp');
var reactify = require('reactify');
var source = require('vinyl-source-stream');

var paths = {
  app_jsx: ['./src/js/app.jsx'],
  js: ['src/js/*.js'],
};

gulp.task('clean', function(done) {
  del(['build'], done);
});

gulp.task('js', ['clean'], function() {
  browserify(paths.app_jsx)
    .transform(reactify)
    .bundle()
    .pipe(source('bundle.js'))
    .pipe(gulp.dest('./build/'));
});

gulp.task('watch', function() {
  gulp.watch(paths.js, ['js']);
});

gulp.task('default', ['watch', 'js']);
