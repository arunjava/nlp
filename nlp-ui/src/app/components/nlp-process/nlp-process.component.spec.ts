import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NlpProcessComponent } from './nlp-process.component';

describe('NlpProcessComponent', () => {
  let component: NlpProcessComponent;
  let fixture: ComponentFixture<NlpProcessComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [NlpProcessComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(NlpProcessComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
