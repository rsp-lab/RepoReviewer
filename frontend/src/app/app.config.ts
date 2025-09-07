import { ApplicationConfig, provideBrowserGlobalErrorListeners, provideZoneChangeDetection } from '@angular/core';
import { provideRouter } from "@angular/router";
import { provideHttpClient } from "@angular/common/http";
import { provideCharts, withDefaultRegisterables } from 'ng2-charts';
import { routes } from "./app.routes";

export const appConfig: ApplicationConfig = {
    providers: [
        /*
            W aplikacji używamy standalone components, czyli:
            nie muszę dzięki temu robić w NgModule:
            imports: [HttpClientModule, RouterModule.forRoot(routes)] itd.

            Wstrzykuje HttpClient globalnie, aby można go było wstrzykiwać
             w serwisach.
            Rejestruje i konfiguruje Router globalnie.
        */
        provideRouter(routes),
        provideHttpClient(),
        provideBrowserGlobalErrorListeners(),
        /*
            Opcjonalne, angular i tak używa NgZone do wykrywania zmian, tutaj tylko
            ustawiasz eventCoalescing dla wykrywania zmian. Zamiast wykrywać zmiany
            po każdym zdarzeniu (kliknięcie, timer, input itp.), co jest nieefektywne,
            lepiej jest łączyć wiele zdarzeń w jeden cykl wykrywania zmian.

            Dobre dla dużych aplikacji co muszą działać wydajnie.
            Chyba że chcesz szybkiej reakcji na zmiany, wtedy łączenie może nie być ok.
            Plus z debugowaniem może być gorzej wykrywać pojedyńcze zmiany.
        */
        provideZoneChangeDetection({ eventCoalescing: true }),
        provideCharts(withDefaultRegisterables())
    ]
};
