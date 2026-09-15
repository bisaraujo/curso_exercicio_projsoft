name: Deploy

on:
  push:
    branches:
      - master

jobs:
  deploy:
    runs-on: ubuntu-latest

    steps:
      - name: Checkout repository
        uses: actions/checkout@v4

      - name: Setup Java 17
        uses: actions/setup-java@v4
        with:
          distribution: temurin
          java-version: '17'
          cache: maven

      - name: Run tests
        run: ./mvnw clean test

      - name: Login to Docker Hub
        uses: docker/login-action@v3
        with:
          username: ${{ secrets.DOCKER_USERNAME }}
          password: ${{ secrets.DOCKER_TOKEN }}

      - name: Build Docker image
        run: docker build -t ${{ secrets.DOCKER_USERNAME }}/curso-projsoft:latest .

      - name: Push Docker image
        run: docker push ${{ secrets.DOCKER_USERNAME }}/curso-projsoft:latest

      - name: Deploy to AWS
        uses: appleboy/ssh-action@v1
        env:
          DB_URL: ${{ secrets.DB_URL }}
          DB_USERNAME: ${{ secrets.DB_USERNAME }}
          DB_PASSWORD: ${{ secrets.DB_PASSWORD }}
          DOCKER_USERNAME: ${{ secrets.DOCKER_USERNAME }}
        with:
          host: ${{ secrets.AWS_HOST }}
          username: ${{ secrets.AWS_USER }}
          key: ${{ secrets.AWS_SSH_KEY }}
          envs: DB_URL,DB_USERNAME,DB_PASSWORD,DOCKER_USERNAME
          script: |
            docker pull $DOCKER_USERNAME/curso-projsoft:latest

            docker rm -f curso-app || true

            docker run -d \
              --name curso-app \
              --network curso-network \
              -p 8082:8080 \
              -e DB_URL="$DB_URL" \
              -e DB_USERNAME="$DB_USERNAME" \
              -e DB_PASSWORD="$DB_PASSWORD" \
              $DOCKER_USERNAME/curso-projsoft:latest