define(['angular'], function(angular) {

// ************************************************************************************************************************************************
// *************************************************************** MODAL - Controller *************************************************************
// ************************************************************************************************************************************************


    var modalController = ["$scope", "$http", "Uri", "Notifications", "userName", "$window", "$modalInstance", function($scope, $http, Uri, Notifications, userName, $window, $modalInstance) {

        $scope.userName = userName;

        //GET
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        //********************************************
        //Funktion um alle Tags eines Users auszulesen
        //********************************************

        function getAllTagsFromUser() {
            $http.get(Uri.appUri("plugin://social/:engine/tags/" + userName))
                .success(function (data) {
                    $scope.dataSets = data;
                    console.log($scope.dataSets);
                })
                .error(function (data, status, header, config) {
                });
        }

        $scope.gotoProcess = function (processId) {
            $window.open($('base').attr('href') + '#/process-definition/' + processId);
            return false;
        }


        $scope.closeUserModal = function () {
            $modalInstance.close();
        }

    getAllTagsFromUser();

    }];

// ************************************************************************************************************************************************
// *************************************************************** TAB - Controller ***************************************************************
// ************************************************************************************************************************************************


    var TabController = ["$scope", "$http", "Uri", "Notifications", function($scope, $http, Uri, Notifications) {

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //GET
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

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

    //*******************************************
    //User für eine Process-Defintion-ID auslesen
    //*******************************************
    function getUsers() {
        $http.get(Uri.appUri("plugin://social/:engine/" + $scope.processDefinition.id + "/users"))
            .success(function (data) {
                $scope.processUsers = data;
                console.log($scope.processUsers);
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

    //POST
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //*****************************************
    //Tag für eine Process-Defintion-ID anlegen
    //*****************************************
    $scope.setTag = function(tag) {
        $http.post(Uri.appUri("plugin://social/:engine/" + $scope.processDefinition.id + "/tags/"+tag.name))
            .success(function () {
                getTags();
                getUsers();
                getPosts();

                var status = '# Tag created #';
                var message = tag.name + ' added to process!';

                Notifications.addMessage({
                    status: status,
                    message: message,
                    http: true,
                    exclusive: [ 'http' ],
                    duration: 10000
                });
                $scope.tag.name = null;
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
                getUsers();
                getTags();

                var status = '# Post created #';
                var message = post.caption + ' added to process!';

                Notifications.addMessage({
                    status: status,
                    message: message,
                    http: true,
                    exclusive: [ 'http' ],
                    duration: 10000
                });
                $scope.post.caption = null;
                $scope.post.name = null;
            })
            .error(function (data, status, header, config) {
            });
    }

     //DELETE
     ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

     //*****************************************
     //Tag für eine Process-Defintion-ID löschen
     //*****************************************
     $scope.deleteTag = function(tag) {
         $http.delete(Uri.appUri("plugin://social/:engine/" + $scope.processDefinition.id + "/tags/"+tag.tagName))
             .success(function () {
                 getTags();
                 getUsers();
                 getPosts();

                 var status = '# Tag erased #';
                 var message = tag.tagName + ' removed from process!';

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

    //************************************************************************************************
    //Funktions Executions
    //************************************************************************************************

    getAllTags();
    getUsers();
    getTags();
    getPosts();

  }];

// ************************************************************************************************************************************************
// *************************************************************** DashboardController ************************************************************
// ************************************************************************************************************************************************

  var DashboardController = ["$scope", "$http", "Uri", "$modal", "$window", function($scope, $http, Uri, $modal, $window) {

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
    //Funktion um alle User auszulesen
    //********************************

    function getAllUsers() {
        $http.get(Uri.appUri("plugin://social/:engine/users"))
            .success(function(data) {
                $scope.users = data;
                console.log($scope.users);
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

    getAllUsers();
    getAllTags();
    getAllPosts();


   $scope.openUserModal = function(user) {
       var modalInstance = $modal.open({
           templateUrl: $('base').attr('cockpit-api') + 'plugin/social/static/app/modalUser.html',
           controller: modalController,
           scope: $scope,
           size: 'lg',
           resolve: {
               userName: function () {
                   return user;
               }
           }
       });
   };

   $scope.gotoProcess = function (processId) {
       $window.open($('base').attr('href') + '#/process-definition/' + processId);
       return false;
   }

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

  var ngModule = angular.module('cockpit.plugin.social', ["ui.bootstrap"])
      .filter('split', function() {
          return function (input, splitChar, splitIndex) {
              // do some bounds checking here to ensure it has that index
              return input.split(splitChar)[splitIndex];
          };
      });

  ngModule.config(Configuration);

  return ngModule;
});
