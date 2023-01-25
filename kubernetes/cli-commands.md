How to load docker image in minikube

    minikube image load <image-name>

Get basic info about k8s components

    kubectl get node
    kubectl get pod
    kubectl get svc
    kubectl get all

Setting the namespace preference. You can permanently save the namespace for all subsequent kubectl commands in that context.

	kubectl config set-context --current --namespace=<insert-namespace-name-here>

Execute commands in database

    kubectl exec <pod> -- mariadb -u<username> -p<password> -e "<QUERY>"

Execute commands in pod

 	kubectl exec -it pod/<pod> /bin/bash