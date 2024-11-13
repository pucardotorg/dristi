FROM node:20-alpine
ARG WORK_DIR
WORKDIR /app
COPY ${WORK_DIR} .

RUN npm install
ENV PORT=8080
EXPOSE 8080

CMD ["npm", "start"]
