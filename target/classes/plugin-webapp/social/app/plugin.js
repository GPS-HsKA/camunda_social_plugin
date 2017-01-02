/**
 * Created by Goetz Phillipp Schmidt
 * // ************************************************************************************************************************************************
 * // *************************************************************** Camunda - Social - Plugin ******************************************************
 * // ************************************************************************************************************************************************
 */

'use strict';

define(['angular', './components/dashboard/dashboardController', './components/processdefinition/processdefinitionController'], function (angular, dashboardController, processdefinitionController) {

    var ngModule = angular.module('cockpit.plugin.social', ['ui.bootstrap'])
        .filter('split', function () {
            return function (input, splitChar, splitIndex) {
                return input.split(splitChar)[splitIndex];
            };
        });

    ngModule.config(function (ViewsProvider) {

        ViewsProvider.registerDefaultView('cockpit.processDefinition.runtime.tab', {
            id: 'process-definitions',
            label: 'Social Interactions',
            url: 'plugin://social/static/app/components/processdefinition/processdefinition.html',
            controller: processdefinitionController,
            priority: 12
        });

        ViewsProvider.registerDefaultView('cockpit.dashboard', {
            id: 'process-definitions',
            label: 'Social Interactions',
            url: 'plugin://social/static/app/components/dashboard/dashboard.html',
            controller: dashboardController,
            priority: 12
        });
    });

    ngModule.controller('ProcessDefinitionController', processdefinitionController);
    ngModule.controller('DashboardController', dashboardController);

    return ngModule;

});
