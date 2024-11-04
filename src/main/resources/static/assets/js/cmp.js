	(function ($) {
		
    var serialize = $.fn.serialize;

    $.fn.serialize = function () {
        let values = serialize.call(this);
        let checkboxes = [];

        checkboxes = checkboxes.concat(
            $('input[type=checkbox]:not(:checked)', this).map(
            function () {
                return this.name + '=false';
            }).get()
        );

        if(checkboxes.length > 0)
            values = checkboxes.join('&') + '&' + values;

        return values;
    };

})(jQuery);

    function ajaxSessionTimeout(jqXHR, _textStatus, _errorThrown)
	{
		switch (jqXHR.status) {
		  case 500:
		    alert(jqXHR.responseJSON.message);
		    break;
		  case 401:
		    alert(jqXHR.responseJSON.message);
		    break;
		  case 415:
		    alert(jqXHR.responseText);
		    break;
		  case 404:
		    alert("Error 404, Resource not found");
		    break;
		  case 405:
		    alert(jqXHR.responseText);
		    break;
		  case 901:
		    alert("Session expired:" + jqXHR.status);
		    break;
		  default:
		    alert("A problem occured:" + jqXHR.status);
		    break;
		}
	}


		function post(forma, action)
		{
			var post_url = action;
			var request_method = forma.method; //get form GET/POST method
			var form_data = $(forma).serialize(); //Encode form elements for submission
			$(':input,:checkbox,:radio').removeClass('is-valid').removeClass('is-invalid');

			$.ajax({
				url : post_url,
				type: request_method,
				data : form_data,
          		headers: {
              		"accept": "application/json",
              		"Access-Control-Allow-Origin":"*"
              		}
			}).done(function(response){
				var arreglo_campos = response.form.fields;
				console.log(arreglo_campos);
				var valido = true;
				arreglo_campos.forEach(function (campo) {
					if (!campo.valid){
				    	valido = false;
				    	$('#'+campo.fieldName).removeClass('is-valid').addClass('is-invalid');
						$('#'+campo.fieldName).siblings('.invalid-feedback').text(campo.error);
				    }else{
				    	$('#'+campo.fieldName).removeClass('is-invalid').addClass('is-valid');
				    	$('#'+campo.fieldName).val(campo.value);
				    }
				});
				$('#res_message').html(response.form.instruction);
				if (valido){
					$('#botones').html('');
					response.form.buttons.forEach(function (accion) {
						$('#botones').append(accion.sourceCode);
						$('#botones').append('&nbsp;');
					});
			        if(window.childFunction) {
			        	childFunction(forma, response.form);
			        }
			        if (response.code){
			        	eval(response.code);
			        }
			    }
			}).fail(function (jqXHR, textStatus, errorThrown) {
			 	ajaxSessionTimeout(jqXHR, textStatus, errorThrown);
			}).always(function() {
               // unblock when remote call returns
			});
		};
		
		function update(field, action, destfield, filterfield)
		{
					var post_url = action;
					var request_method = "post";
					const form_data = {ajax_origin: field.name, ajax_value: field.value, ajax_dest: destfield, ajax_filter: filterfield};
					//console.log(form_data);
					$(':input,:checkbox,:radio').removeClass('is-valid').removeClass('is-invalid');

					$.ajax({
						url : post_url,
						type: request_method,
						data : form_data,
		          		headers: {
		              		"accept": "application/json",
		              		"Access-Control-Allow-Origin":"*"
		              		}
					}).done(function(response){
						var arreglo_campos = response.form.fields;
						console.log(arreglo_campos);
						$("#" + destfield).parent().replaceWith(arreglo_campos[0].sourceCode);
						$('#' + destfield).removeClass('is-invalid').removeClass('is-valid');
					}).fail(function (jqXHR, textStatus, errorThrown) {
					 	ajaxSessionTimeout(jqXHR, textStatus, errorThrown);
					}).always(function() {
		               // unblock when remote call
					});
		};