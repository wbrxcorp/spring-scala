var app = angular.module("singlejs", [])
    .run(["$rootScope", function($scope) {
        $scope.js_name = window["js-name"];
    }]);
