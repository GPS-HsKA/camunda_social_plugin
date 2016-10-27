define(['angular'], function(angular) {

  var DashboardController = ["$scope", "$http", "Uri", function($scope, $http, Uri) {

    $http.get(Uri.appUri("plugin://social/:engine/process-instance"))
        .success(function(data) {
          $scope.processInstanceCounts = data;
        });
  }];

  var Configuration = ['ViewsProvider', function(ViewsProvider) {

    ViewsProvider.registerDefaultView('cockpit.processDefinition.runtime.tab', {
      id: 'process-definitions',
      label: 'Social Prozesse',
      url: 'plugin://social/static/app/dashboard.html',
      controller: DashboardController,

      // make sure we have a higher priority than the default plugin
      priority: 12
    });
  }];

  var ngModule = angular.module('cockpit.plugin.social', []);

  ngModule.config(Configuration);

  return ngModule;
});
