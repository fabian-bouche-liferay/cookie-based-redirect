# Cookie based automatic redirection

This projects implements a module that automatically redirects a user if the following conditions are met:
 
* The Filter is enabled
* A given cookie is present in the request
* The virtual host of the initial request has been white listed

A sendRedirect on the HttpServletResponse is triggered when those conditions are true.

The configuration of those conditions is done through System Settings -> Third party -> Cookie triggered redirection 