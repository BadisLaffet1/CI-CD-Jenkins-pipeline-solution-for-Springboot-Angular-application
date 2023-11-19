FROM node:16

RUN npm install -g @angular/cli

WORKDIR /app

COPY package*.json ./

RUN npm install

COPY . .

RUN ng build --configuration=production

EXPOSE 4200

CMD ["ng", "serve", "--host", "0.0.0.0", "--port", "4200"]

