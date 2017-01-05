/**
 * Created by Goetz Phillipp Schmidt
 * // ************************************************************************************************************************************************
 * // *************************************************************** PROCESSDEFINITION - Controller ***************************************************************
 * // ************************************************************************************************************************************************
 */

'use strict';

define(['angular', '../modal/postModalController'], function(angular, postmodalController) {

    return ["$scope", "$http", "Uri", "Notifications", "$modal", function($scope, $http, Uri, Notifications, $modal) {

    //GET
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //********************************
    //Funktion um alle Tags auszulesen
    //********************************

    function getAllTags() {
        $http.get(Uri.appUri("plugin://social/:engine/tags"))
            .success(function(data) {
                $scope.tags = data;
            })
            .error(function (data, status, header, config) {
            });
    }

    //****************************************
    //Funktion um alle BPMN2.0 Tags auszulesen
    //****************************************

    function getAllBpmnTags() {
        $http.get(Uri.appUri("plugin://social/:engine/" + $scope.processDefinition.id + "/" + $scope.processDefinition.key))
            .success(function(data) {
                $scope.bpmnTags = data;
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
            });
    }

    //*******************************************
    //User für eine Process-Defintion-ID auslesen
    //*******************************************
    function getUsers() {
        $http.get(Uri.appUri("plugin://social/:engine/" + $scope.processDefinition.id + "/users"))
            .success(function (data) {
                $scope.processUsers = data;
            });
    }

    //************************************************
    //Blogposts für eine Process-Defintion-ID auslesen
    //************************************************
    function getPosts() {
        $http.get(Uri.appUri("plugin://social/:engine/" + $scope.processDefinition.id + "/blog"))
            .success(function (data) {
                $scope.processPosts = data;
            });
    }

    //POST
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //*****************************************
    //Tag für eine Process-Defintion-ID anlegen
    //*****************************************
    $scope.setTag = function(tag) {
        $http.post(Uri.appUri("plugin://social/:engine/" + $scope.processDefinition.id + "/tags/" + $scope.processDefinition.key + "/" + tag.name))
            .success(function () {

                $scope.update();

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
    };

    //*****************************************
    //Blogpost anlegen
    //*****************************************
    $scope.setPost = function(post) {
        $http.post(Uri.appUri("plugin://social/:engine/" + $scope.processDefinition.id + "/blog/" +post.caption+ "/" +post.name+ "/false"))
            .success(function () {

                $scope.update();

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
    };


    //DELETE
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //*****************************************
    //Tag für eine Process-Defintion-ID löschen
    //*****************************************

    $scope.deleteTagModal = function (tag) {
        var modalInstance = $modal.open({
            templateUrl: $('base').attr('cockpit-api') + 'plugin/social/static/app/components/modal/modalPost.html',
            controller: postmodalController,
            scope: $scope,
            size: 'lg',
            resolve: {
                tag: function () {
                    return tag;
                }
            }
        });
        modalInstance.result.then(function () {
            $scope.update();
        });
    };

    //************************************************************************************************
    //Funktions Executions
    //************************************************************************************************

    $scope.update = function () {
        getAllBpmnTags();
        getPosts();
        getUsers();
        getTags();
    };

    getAllTags();
    getAllBpmnTags();
    getUsers();
    getTags();
    getPosts();

}]

});