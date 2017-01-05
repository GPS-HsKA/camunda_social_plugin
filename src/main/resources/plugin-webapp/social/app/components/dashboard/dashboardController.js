/**
 * Created by Goetz Phillipp Schmidt
 * // ************************************************************************************************************************************************
 * // *************************************************************** Dashboard - Controller ***************************************************************
 * // ************************************************************************************************************************************************
 */

'use strict';

define(['angular', '../modal/tagModalController', '../modal/userModalController'],  function (angular, tagmodalController, usermodalController) {

    return ["$scope", "$http", "Uri", "$modal", "$window", function($scope, $http, Uri, $modal, $window) {

    //******************************************
    //Funktion um alle Datenbank Tags auszulesen
    //******************************************

    function getAllTags() {
        $http.get(Uri.appUri("plugin://social/:engine/tags"))
            .success(function(data) {
                $scope.tags = data;
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
            templateUrl: $('base').attr('cockpit-api') + 'plugin/social/static/app/components/modal/modalUser.html',
            controller: usermodalController,
            scope: $scope,
            size: 'lg',
            resolve: {
                userName: function () {
                    return user;
                }
            }
        });
    };

    $scope.openTagModal = function(tag) {
        var modalInstance = $modal.open({
            templateUrl: $('base').attr('cockpit-api') + 'plugin/social/static/app/components/modal/modalTag.html',
            controller: tagmodalController,
            scope: $scope,
            size: 'lg',
            resolve: {
                tagName: function () {
                    return tag;
                }
            }
        });
    };

    $scope.gotoProcess = function (processId) {
        $window.open($('base').attr('href') + '#/process-definition/' + processId);
        return false;
    }

    $scope.statusIconMapping = {
        "true": "half_circle.jpg",
        "false": "full_circle.jpg"
    };

    }]

});