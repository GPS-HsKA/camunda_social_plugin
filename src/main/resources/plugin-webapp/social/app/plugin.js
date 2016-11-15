define(['angular'], function(angular) {

  // *************************************************************** TabController ************************************************************

  var TabController = ["$scope", "$http", "$filter", "Uri", "Notifications", function($scope, $http, $filter, Uri, Notifications) {

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //GET
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //*******************************************
    //Tags für eine Process-Defintion-ID auslesen
    //*******************************************
    function getTags() {
      $http.get(Uri.appUri("plugin://social/:engine/" + $scope.processDefinition.id + "/tags"))
          .success(function (data) {
            $scope.processTags = data;
            console.log($scope.processTags);
          });
    }

    //************************************************
    //Blogposts für eine Process-Defintion-ID auslesen
    //************************************************
    function getPosts() {
        $http.get(Uri.appUri("plugin://social/:engine/" + $scope.processDefinition.id + "/blog"))
            .success(function (data) {
                $scope.processPosts = data;
                console.log($scope.processPosts);
            });
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //POST
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //*****************************************
    //Tag für eine Process-Defintion-ID anlegen
    //*****************************************
    $scope.setTag = function(tag) {
        $http.post(Uri.appUri("plugin://social/:engine/" + $scope.processDefinition.id + "/tags/"+tag.name))
            .success(function () {
                getTags();

                var status = '# Tag created #';
                var message = tag.name + ' added to process!';

                Notifications.addMessage({
                    status: status,
                    message: message,
                    http: true,
                    exclusive: [ 'http' ],
                    duration: 10000
                });
            })
            .error(function (data, status, header, config) {
            });
    }

    //*****************************************
    //Blogpost anlegen
    //*****************************************
    $scope.setPost = function(post) {
        $http.post(Uri.appUri("plugin://social/:engine/" + $scope.processDefinition.id + "/blog/" +post.caption+ "/" +post.name))
            .success(function () {
                getPosts();

                var status = '# Post created #';
                var message = post.caption + ' added to process!';

                Notifications.addMessage({
                    status: status,
                    message: message,
                    http: true,
                    exclusive: [ 'http' ],
                    duration: 10000
                });
            })
            .error(function (data, status, header, config) {
            });
    }

    getTags();
    getPosts();

  }];

  // *************************************************************** DashboardController ************************************************************

  var DashboardController = ["$scope", "$http", "$filter", "Uri", function($scope, $http, $filter, Uri) {

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

      //********************************
      //Funktion um alle Posts auszulesen
      //********************************

      function getAllPosts() {
          $http.get(Uri.appUri("plugin://social/:engine/blog"))
              .success(function(data) {
                  $scope.posts = data;
                  console.log($scope.posts);
              })
              .error(function (data, status, header, config) {
              });
      }

      getAllTags();
      getAllPosts();

  }];

  var Configuration = ['ViewsProvider', function(ViewsProvider) {

    ViewsProvider.registerDefaultView('cockpit.processDefinition.runtime.tab', {
      id: 'process-definitions',
      label: 'Social Pool',
      url: 'plugin://social/static/app/tab.html',
      controller: TabController,

      // make sure we have a higher priority than the default plugin
      priority: 12
    });

    ViewsProvider.registerDefaultView('cockpit.dashboard', {
      id: 'process-definitions',
      label: 'Social Pool',
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
