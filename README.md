![foro](https://github.com/matexxe/Literatura-consola-app/assets/158209261/3900215b-fb55-48b5-8a12-fbb1daa5f26c)

## Desafio - Forohub de Oracle Next Education de Alura Latam 🖥️ 📨
Para este reto, se nos pide realizar una aplicacion de consola semejante a un foro donde el usuario registrado debe pasar por una autentificacion
primero para acceder a los endpoint que esten protegios mediante un token. 
Esta aplicacion nos permite tener una produccion segura a fin de que usuarios no autenticados no puedan modificiar **topicos (temas de discusion)** sin permisos suficientes. 


> _IMPORTANTE_

> Usar variables de entorno para la base de datos, asegura proteccion a datos vulnerables que hayan sido registrado de ataques corruptos. 



Para ampliar mas el contexto de los *foros*:

Puede definirse como un espacio de encuentro entre diversos participantes con el objetivo de intercambiar opiniones, plantear preguntas en torno
a un tema o subtemas de interés común, así como compartir habilidades, experiencias o respuestas a preguntas.

## Demostracion 

Luego de autenticar al usuario, generamos el token con el cual tendremos acceso a los topicos protegidos. 

![img 4](https://github.com/matexxe/Literatura-consola-app/assets/158209261/6ff2cbab-6837-499f-88ac-dd39fa6e11ad)

El token sirve como la identidad que describe al usuario registrado. Luego podra hacer la funcion basica de CRUD.

**Para traer los topicos registrados en la base de datos:** Usamos la direccion: **http://localhost:8080/topic** y el metodo **GET**.

![imagn 5](https://github.com/matexxe/Literatura-consola-app/assets/158209261/17012210-2636-4132-b081-dee3e905f49f)

**Para eliminar un topico especifico de la base de datos, se detalla el id del topico a eliminar:** 

No olvidemos agregar el **DELETE** con la misma direccion ya que el mapeo de los topicos esta manejado con el prefijo *topic*.

**Se elimna el topic con el id 2**.

![Imgn 6](https://github.com/matexxe/Literatura-consola-app/assets/158209261/6d1136cf-1a3a-4388-a773-66fc8536d7a6)

Visualizando la base de datos: 

![bd_1](https://github.com/matexxe/Literatura-consola-app/assets/158209261/76223ea7-6f91-4408-89e4-57d55d8becc5)

## Tecnologias utilizadas
- Spring Boot
- JPA (Java Persistence)
- Spring Security
- JWT (JSON Web Token)
- Spring Web
- Flyway
- MySQL
- Lombok
- Postman












