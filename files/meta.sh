declare -a arr=("docker pull alpine"
                "docker pull busybox"
                "docker pull nginx"
                "docker pull ubuntu"
                "docker pull python"
                "docker pull redis"
                "docker pull postgres"
                "docker pull node"
                "docker pull httpd"
                "docker pull mongo")
for i in "${arr[@]}"
do
    web1=https://registry.hub.docker.com/v2/repositories/library/${i:12}/tags?page_size=1024
    curl -L -s ${web1}|jq '."results"[]["name"]' > ${i:12}.txt
    curl -L -s ${web1}|jq '."results"' > meta/${i:12}.json
    echo "Version is done <<<<<<<<<<<<<<<<<<<<<<<<"
done