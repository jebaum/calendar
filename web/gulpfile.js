var autoprefix = require('gulp-autoprefixer');
var browserify = require('browserify');
var del = require('del');
var gulp = require('gulp');
var notify = require('gulp-notify');
var reactify = require('reactify');
var sass = require('gulp-ruby-sass');
var source = require('vinyl-source-stream');

var paths = {
  app_jsx: ['./src/js/app.jsx'],
  js: ['./src/js/*.js'],
  sass: ['./src/sass/*.scss'],
};

gulp.task('clean', function(done) {
  del(['build'], done);
});

gulp.task('js', function() {
  browserify(paths.app_jsx)
    .transform(reactify)
    .bundle()
    .pipe(source('bundle.js'))
    .pipe(gulp.dest('./build/'));
});

gulp.task('watch', function() {
  gulp.watch(paths.js, ['js']);
  gulp.watch(paths.sass, ['css']);
});

gulp.task('css', function() {
  return gulp.src(paths.sass)
    .pipe(sass({
        style: 'nested',
        loadPath: ['./src/sass']
      })
      .on("error", notify.onError(function(error) {
        return "Error: " + error.message;
      })))
    .pipe(gulp.dest('./build/'));
});

gulp.task('default', ['clean', 'watch', 'js', 'css']);
