/**
 * Created by ipeleg on 5/13/14.
 */

angular.module('userCompaniesController',[])
    .controller('CompaniesController', ['$scope', '$http',
        function getUserCompaniesList($scope, $http)
        {
            $http.get('/greenbill/rest/company/forUser')
                .success(function (response)
                {
                    $scope.companies  = response;
                });

            function reloadPage()
            {
                window.location.reload();
            }

            function submitRemoveRequest(companyId)
            {
                $.ajax({
                    type: "POST",
                    contentType: "application/json",
                    url: "rest/company/removeUserCompanies",
                    data: JSON.stringify({"id": companyId}),
                    dataType: "json",
                    success: function (data, status, jqXHR) {
                        new PNotify({
                            title: 'Thank You!',
                            text: 'Your changes were made and the companies were notified.',
                            type: 'success'
                        });
                        window.setTimeout(reloadPage,2000) // Wait for 2.0s and reload the page
                    },
                    error: function (xhr) {
                        alert(xhr.responseText);
                    }
                });
            }

            $scope.askToSubmit = function (companyId, companyName){
                PNotify.prototype.options.styling = "bootstrap3";
                (new PNotify({
                    title: 'Confirmation Needed',
                    text: 'You are about to unregister from:\n' + companyName + ',\nAre you sure?',
                    icon: 'glyphicon glyphicon-question-sign',
                    hide: false,
                    confirm: {
                        confirm: true
                    },
                    buttons: {
                        closer: false,
                        sticker: false
                    },
                    history: {
                        history: false
                    }
                })).get().on('pnotify.confirm', function()
                    {
                        submitRemoveRequest(companyId);
                    }).on('pnotify.cancel', function()
                    {
                        new PNotify({
                            title: 'Nothing to change',
                            text: 'No changes were made.',
                            type: 'info'
                        });
                    });
            };
        }
    ]);