module.exports = function(config) {
   config.set({
        basePath: 'src',
        frameworks: ['jasmine'],
        files: [
            'https://code.angularjs.org/1.3.15/angular.js',
            'https://code.angularjs.org/1.3.15/angular-mocks.js',
            "https://ajax.googleapis.com/ajax/libs/angularjs/1.3.15/angular-route.js",
            "https://ajax.googleapis.com/ajax/libs/angularjs/1.3.15/angular-resource.js",
            "https://cdnjs.cloudflare.com/ajax/libs/angular-ui-bootstrap/0.12.1/ui-bootstrap-tpls.js",
            "examples/webapp/*.html",
            "examples/webapp/js/*.js",
            "test/ts/*.ts",
            "test/ts/*.js"
        ],
        exclude: [
            '**/*.d.ts'
        ],
        preprocessors: {
            '*.html': 'ng-html2js',
            '**/*.ts': ['typescript']
        },
        typescriptPreprocessor: {
            // options passed to the typescript compiler
            options: {
                sourceMap: false, // (optional) Generates corresponding .map file.
                target: 'ES5', // (optional) Specify ECMAScript target version: 'ES3' (default), or 'ES5'
                module: 'amd', // (optional) Specify module code generation: 'commonjs' or 'amd'
                noImplicitAny: true, // (optional) Warn on expressions and declarations with an implied 'any' type.
                //noResolve: true, // (optional) Skip resolution and preprocessing.
                removeComments: true // (optional) Do not emit comments to output.
            },
            // transforming the filenames
            transformPath: function(path) {
                return path.replace(/\.ts$/, '.js');
            }
        },
        ngHtml2JsPreprocessor: {
            moduleName: 'html-templates'
        },
        autoWatch: true
    });
};
