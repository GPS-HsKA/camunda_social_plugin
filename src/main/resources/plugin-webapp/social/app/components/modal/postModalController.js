/**
 * Created by Goetz Phillipp Schmidt
 * // ************************************************************************************************************************************************
 * // *************************************************************** POST - MODAL - Controller ******************************************************
 * // ************************************************************************************************************************************************
 */

'use strict';

define(['angular'], function (angular) {

    return ["$scope", "$http", "Uri", "Notifications", "$modalInstance", "tag", function ($scope, $http, Uri, Notifications, $modalInstance, tag) {

        $scope.tagName = tag.tagName;
        $scope.post = {caption: 'Tag ' + tag.tagName + ' deleted'};

        //GET
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        $scope.closePostModal = function () {
            $modalInstance.close();
        };

        //POST
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        $scope.setPost = function (post) {
            $http.post(Uri.appUri("plugin://social/:engine/" + $scope.processDefinition.id + "/blog/" + post.caption + "/" + post.name + "/true"))
                .success(function () {
                    $scope.deleteTag();
                    $scope.closePostModal();
                })
                .error(function (data, status, header, config) {
                });
        };

        //DELETE
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        $scope.deleteTag = function () {
            $http.delete(Uri.appUri("plugin://social/:engine/" + $scope.processDefinition.id + "/tags/" + tag.tagName))
                .success(function () {
                    var status = '# Tag erased & Post created #';
                    var message = tag.tagName + ' removed from process!';
                    Notifications.addMessage({
                        status: status,
                        message: message,
                        http: true,
                        exclusive: ['http'],
                        duration: 10000
                    });
                })
                .error(function (data, status, header, config) {
                });
        }
    }]
});