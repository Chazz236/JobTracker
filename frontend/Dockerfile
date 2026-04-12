#node.js image
FROM node:20-alpine

#set working directory inside container
WORKDIR /app

#copy package files and install dependencies
COPY package*.json ./
RUN npm install

#copy code
COPY . .

#expose vite port
EXPOSE 5173

#start app
CMD ["npm", "run", "dev", "--", "--host"]