/**
 * Created by Goetz Phillipp Schmidt
 * // ************************************************************************************************************************************************
 * // *************************************************************** TAG - MODAL - Controller ******************************************************
 * // ************************************************************************************************************************************************
 */

'use strict';

define(['angular'], function (angular) {

    return ["$scope", "$http", "Uri", "Notifications", "tagName", "$window", "$modalInstance", function ($scope, $http, Uri, Notifications, tagName, $window, $modalInstance) {

        $scope.tagName = tagName;

        //GET
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        //*******************************************************
        //Funktion um Processdefinitionen zu einem Tag auszulesen
        //*******************************************************

        function getProcessdef(tag) {
            $http.get(Uri.appUri("plugin://social/:engine/" + tag + "/processdefinitions"))
                .success(function (data) {
                    $scope.processdefs = data;
                })
                .error(function (data, status, header, config) {
                });
        }

        $scope.gotoProcess = function (processId) {
            $window.open($('base').attr('href') + '#/process-definition/' + processId);
            return false;
        };

        $scope.closeTagModal = function () {
            $modalInstance.close();
        };

        getProcessdef(tagName);

    }]
});