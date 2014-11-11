var autoprefix = require('gulp-autoprefixer');
var browserify = require('browserify');
var del = require('del');
var gulp = require('gulp');
var less = require('gulp-less');
var reactify = require('reactify');
var source = require('vinyl-source-stream');

var paths = {
  app_jsx: ['./src/js/app.jsx'],
  js: ['./src/js/*.js'],
  less: ['./src/less/style.less'],
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
  gulp.watch(paths.less, ['css']);
});

gulp.task('css', function() {
  return gulp.src(paths.less)
    .pipe(less({
      paths: ['node_modules'],
    }))
    .pipe(gulp.dest('./build/'));
});

gulp.task('fonts', function() {
  return gulp.src('./bower_components/bootstrap-sass-official/assets/fonts/bootstrap/*.*')
    .pipe(gulp.dest('./build/fonts/'));
});

gulp.task('default', ['clean', 'watch', 'js', 'css', 'fonts']);
