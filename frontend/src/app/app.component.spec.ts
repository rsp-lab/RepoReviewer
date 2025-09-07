import { ComponentFixture, TestBed } from '@angular/core/testing';
import { AppComponent } from './app.component';

describe('AppComponent', () => {
    let component: AppComponent;
    let fixture: ComponentFixture<AppComponent>;

    beforeEach(async () => {
        /*
            TestBed <- środowisko testowe Angulara dla komponentów. Angular kompiluje
              HTML i CSS w runtime podczas testów.
        */
        // Importuje komponenty standalone bezpośrednio:
        await TestBed.configureTestingModule({
            imports: [AppComponent]
        }).compileComponents();

        fixture = TestBed.createComponent(AppComponent);
        // Wydobywa instancję komponentu
        component = fixture.componentInstance;
        // Uruchamia cykl życia ngOnInit(), ngAfterViewInit()
        fixture.detectChanges();
    });

    it('should create the component AppComponent', () => {
        expect(component).toBeTruthy();
    });

    it('should have document title "RepoReviewer"', () => {
        expect(document.title).toBe('RepoReviewer');
    });
});
