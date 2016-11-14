define(['angular'], function(angular) {

  // *************************************************************** TabController ************************************************************

  var TabController = ["$scope", "$http", "Uri", function($scope, $http, Uri) {

    //*******************************************
    //Tags für eine Process-Defintion-ID auslesen
    //*******************************************
    function getTag() {
      $http.get(Uri.appUri("plugin://social/:engine/" + $scope.processDefinition.id + "/tags"))
          .success(function (data) {
            $scope.processTags = data;
            console.log($scope.processTags);
          });
    }

    //*****************************************
    //Tag für eine Process-Defintion-ID anlegen
    //*****************************************
    $scope.setTag = function(tag) {
      $http.post(Uri.appUri("plugin://social/:engine/" + $scope.processDefinition.id + "/tags/"+tag.name))
          .success(function () {
              alert("Tag angelegt!" + "  " + "ProcessId: " + $scope.processDefinition.id +  "  " + "Tagname: " + tag.name);
          })
          .error(function (data, status, header, config) {
          });
    }

    getTag();

  }];

  // *************************************************************** DashboardController ************************************************************

  var DashboardController = ["$scope", "$http", "Uri", function($scope, $http, Uri) {

    //********************************
    //Funktion um alle Tags auszulesen
    //********************************

    function getAllTags() {
      $http.get(Uri.appUri("plugin://social/:engine/tags"))
          .success(function(data) {
            $scope.tags = data;
            console.log($scope.tags);
          })
          .error(function (data, status, header, config) {
          });
    }

    getAllTags();


  }];

  var Configuration = ['ViewsProvider', function(ViewsProvider) {

    ViewsProvider.registerDefaultView('cockpit.processDefinition.runtime.tab', {
      id: 'process-definitions',
      label: 'Social Prozesse',
      url: 'plugin://social/static/app/tab.html',
      controller: TabController,

      // make sure we have a higher priority than the default plugin
      priority: 12
    });

    ViewsProvider.registerDefaultView('cockpit.dashboard', {
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
