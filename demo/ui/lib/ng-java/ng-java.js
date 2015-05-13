(function () {
    var hooked = false;
    var hookListeners = [];

    window.javangHook = function () {
        hooked = true;
        while (hookListeners.length) {
            var callback = hookListeners.pop();
            callback();
        }
    };

    window.javangBridge = {};

    window.registerScopeFunction = function (scope, functionName, func) {
        scope['functionName'] = func.apply;
    };

    window.onHook = function (callback) {
        if (hooked) {
            callback();
        } else {
            hookListeners.push(callback);
        }
    }
})();

console.log("ALERT");

var app = angular.module("ng-java", []);

app.directive("ngJavaController", function () {

    function link (localScope, el, attrs) {
        var functionWrappers = {
        };

        onHook(function () {
            javangBridge.registerController(localScope.ngJavaController, localScope, functionWrappers);

            for (key in functionWrappers) {
                localScope[key] = function() {
                    return functionWrappers[key].invoke.apply(functionWrappers[key], arguments);
                }
            }
        });
    }

    return {
        link: link,
        scope: {
            ngJavaController: '@'
        }
    };
});

// Testing

