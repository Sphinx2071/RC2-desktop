$(document).ready(function(){
    console.log("The DOM is fully loaded and ready.");
    $('#authorizationType').change(function(){
        let authType = $(this).val();
        $.ajax({
            url: '/registrationModeList',
            data: { AuthorizationType: authType},
            success: function(registrationModeList){
                $('regMode').empty();
                $.each(registrationModeList, function(index, item){
                    $('regMode').append('<option value="' + item.value + '">' + item.label + '</option>');
                });
            }
        });
    });
});
$(document).ready(function(){
    console.log("If you can read this, something worked");
    $('#authorizationType')
}
