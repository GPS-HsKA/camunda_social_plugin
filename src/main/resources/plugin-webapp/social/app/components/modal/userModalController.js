/**
 * Created by Goetz Phillipp Schmidt
 * // ************************************************************************************************************************************************
 * // *************************************************************** USER - MODAL - Controller ******************************************************
 * // ************************************************************************************************************************************************
 */

'use strict';

define(['angular'], function (angular) {

    return ["$scope", "$http", "Uri", "Notifications", "userName", "$window", "$modalInstance", function ($scope, $http, Uri, Notifications, userName, $window, $modalInstance) {

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
                })
                .error(function (data, status, header, config) {
                });
        }

        $scope.gotoProcess = function (processId) {
            $window.open($('base').attr('href') + '#/process-definition/' + processId);
            return false;
        };


        $scope.closeUserModal = function () {
            $modalInstance.close();
        };

        getAllTagsFromUser();

    }]
});