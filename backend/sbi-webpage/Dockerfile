FROM node:20-alpine
ARG WORK_DIR
WORKDIR /app

COPY ${WORK_DIR} .

RUN npm install

EXPOSE 8080

CMD ["node", "app.js"]